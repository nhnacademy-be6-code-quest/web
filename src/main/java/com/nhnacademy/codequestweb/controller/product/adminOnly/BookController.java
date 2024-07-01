package com.nhnacademy.codequestweb.controller.product.adminOnly;


import com.nhnacademy.codequestweb.request.product.bookProduct.BookProductRegisterRequestDto;
import com.nhnacademy.codequestweb.request.product.bookProduct.BookProductUpdateRequestDto;
import com.nhnacademy.codequestweb.response.product.book.AladinBookResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.common.ProductUpdateResponseDto;
import com.nhnacademy.codequestweb.service.product.BookProductService;
import jakarta.validation.Valid;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/products/book")
public class BookController {
    private final BookProductService bookProductService;

    private final MessageSource messageSource;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/register")
    public String registerForm(Model model){

        model.addAttribute("view", "bookProductRegisterForm");

        return "index";
    }

    //register form 외에서는 호출 불가함. 자바스크립트로 통제해놓음
    @GetMapping("/aladinList")
    public String getAladinBookList(@RequestParam(name = "page", required = false)Integer page, @RequestParam("title")String title, Model model) {

        ResponseEntity<Page<AladinBookResponseDto>> aladinBookPageResponse = bookProductService.getBookList(page, title);

        Page<AladinBookResponseDto> aladinBooks = aladinBookPageResponse.getBody();
        if(aladinBooks != null){
            long totalElements = aladinBooks.getTotalElements();
            int totalPages = aladinBooks.getTotalPages();
            if (totalPages != 0){
                List<AladinBookResponseDto> aladinBookList = aladinBooks.getContent();
                model.addAttribute("bookList", aladinBookList);
                Set<Integer> pageNumSet = new LinkedHashSet<>();
                if (page == null){
                    page = 1;
                }
                for (int i = 1; i <= totalPages; i++){
                    if (i == 1 || (page - 2 <= i && i <= page + 2) || i == totalPages){
                        pageNumSet.add(i);
                    }
                }
                model.addAttribute("pageNumSet", pageNumSet);
            } else {
                model.addAttribute("empty", true);
            }
            if (totalElements == 100){
                model.addAttribute("warning", true);
            }
        }else {
            model.addAttribute("empty", true);
            return "index";
        }

        log.info("page : {}, size: {} , tatal page : {}, total elements : {}", page, aladinBooks.getSize(), aladinBooks.getTotalPages(), aladinBooks.getTotalElements());

        model.addAttribute("title",title);
        return "view/product/aladinBookList";
    }


    @PostMapping("/register")
    public String saveBook(@ModelAttribute @Valid BookProductRegisterRequestDto dto, BindingResult bindingResult){
        log.error("product name : {},", dto.productName());
        log.error("isbn 13: {}", dto.isbn13());
        log.error("isbn 10: {}", dto.isbn());
        if (bindingResult.hasErrors()){
            StringBuilder stringBuilder = new StringBuilder();
            List<ObjectError> errors = bindingResult.getAllErrors();
            for (ObjectError error : errors){
                String errorCode = error.getCode();
                log.error("errorCode : {}, locale : {}", errorCode, LocaleContextHolder.getLocale());
                String errorMessage = messageSource.getMessage(Objects.requireNonNull(errorCode), null, error.getDefaultMessage(), LocaleContextHolder.getLocale());
                stringBuilder.append(errorMessage);
            }
            throw new RuntimeException("errors : " + stringBuilder.toString());
        }
        ResponseEntity<ProductRegisterResponseDto> responseEntity = bookProductService.saveBook(dto);
        return "redirect:/";
    }

    @PostMapping("/update")
    public String updateBook(@ModelAttribute @Valid BookProductUpdateRequestDto dto){
        log.info("update book called save book");
        ResponseEntity<ProductUpdateResponseDto> responseEntity = bookProductService.updateBook(dto);
        log.info("status code : {}",responseEntity.getStatusCode().value());
        return "redirect:/";
    }
}
