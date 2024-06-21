package com.nhnacademy.codequestweb.controller.admin;


import com.nhnacademy.codequestweb.client.product.bookProduct.BookProductRegisterClient;
import com.nhnacademy.codequestweb.request.bookProduct.BookProductRegisterRequestDto;
import com.nhnacademy.codequestweb.response.product.AladinBookListResponseDto;
import com.nhnacademy.codequestweb.response.product.AladinBookResponseDto;
import com.nhnacademy.codequestweb.response.product.BookProductRegisterResponseDto;
import com.nhnacademy.codequestweb.response.product.Category;
import com.nhnacademy.codequestweb.response.product.CategoryGetResponseDto;
import com.nhnacademy.codequestweb.response.product.Tag;
import com.nhnacademy.codequestweb.response.product.TagGetResponseDto;
import com.nhnacademy.codequestweb.service.admin.BookProductRegisterService;
import com.nhnacademy.codequestweb.service.admin.CategoryRegisterService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/products/book")
public class BookController {
    private final BookProductRegisterService bookProductRegisterService;


    @GetMapping("/register")
    public String registerForm(Model model){
        List<CategoryGetResponseDto> categoryList = bookProductRegisterService.getCategories().getBody();
        model.addAttribute("categoryList", categoryList);
        log.warn("categoryList: {}", categoryList);

        List<TagGetResponseDto> tagList = bookProductRegisterService.getTags().getBody();
        model.addAttribute("tagList", tagList);
        model.addAttribute("view", "bookProductRegisterForm");

        return "index";
    }

    //register form 외에서는 호출 불가함. 자바스크립트로 통제해놓음
    @GetMapping
    @RequestMapping("/aladinList")
    public String getAladinBookList(@RequestParam("title")String title, Model model) {
        log.error("test called + title : {}", title);
        ResponseEntity<AladinBookListResponseDto> aladinBookListResponseDtoResponseEntity = bookProductRegisterService.getBookList(title);

        AladinBookListResponseDto aladinBookListResponseDto = aladinBookListResponseDtoResponseEntity.getBody();
        if (aladinBookListResponseDto != null) {
            List<AladinBookResponseDto> bookList = aladinBookListResponseDto.getBooks();
            model.addAttribute("bookList", bookList);
        }

        return "view/admin/aladinBookList";
    }


    @PostMapping("/register")
    public String saveBook(@ModelAttribute BookProductRegisterRequestDto dto){
        log.info("add book called save book");
        ResponseEntity<BookProductRegisterResponseDto> responseEntity = bookProductRegisterService.saveBook(dto);
        log.info("status code : {}",responseEntity.getStatusCode().value());
        return "redirect:/";
    }
}
