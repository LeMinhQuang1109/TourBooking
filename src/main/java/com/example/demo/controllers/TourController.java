package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.models.Tour;
import com.example.demo.models.TourDto;
import com.example.demo.services.TourRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/tours")
public class TourController {

    @Autowired
    public TourRepository repo;

    @GetMapping({"", "/"})
    public String showProductList(Model model) {
        // Lấy danh sách tất cả các tour từ database
        List<Tour> tours = repo.findAll();
        
        // Thêm danh sách tours vào model với key "tours"
        model.addAttribute("tours", tours);

        // Trả về view "tours/index"
        return "tours/index";
    }
    
    @GetMapping("/create")
    public String showCreateTour(Model model) {
    	TourDto tourDto = new TourDto(); // Đảm bảo khởi tạo đối tượng TourDto
    	model.addAttribute("tourDto", tourDto);  // Thêm vào model với key "tourDto"
    	return "tours/CreateTour";
    }
    
    @PostMapping("/create")
    public String createTour(@Valid @ModelAttribute TourDto tourDto, BindingResult result) {
        if (result.hasErrors()) {
            // Nếu có lỗi khi validate, quay lại trang tạo tour với lỗi
            return "tours/CreateTour";
        }

        // Chuyển đổi TourDto sang Tour để lưu vào cơ sở dữ liệu
        Tour tour = new Tour();
        tour.setName(tourDto.getName());
        tour.setDescription(tourDto.getDescription());
        tour.setPrice(tourDto.getPrice());
        tour.setDuration(tourDto.getDuration());
        tour.setLocation(tourDto.getLocation());

        // Lưu tour vào cơ sở dữ liệu
        repo.save(tour);

        // Quay lại trang danh sách các tour
        return "redirect:/tours";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditTour(Model model, @PathVariable int id) {
        try {
            Tour tour = repo.findById(id).orElseThrow(() -> new Exception("Tour không tìm thấy"));
            model.addAttribute("tour", tour);  // Đảm bảo tour được thêm vào model
            return "tours/EditTour";  // Đảm bảo trả về view "tours/EditTour"
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/tours";  // Nếu có lỗi, quay lại trang danh sách tour
        }
    }

    
    @PostMapping("/edit")
    public String updateProduct(@Valid @ModelAttribute TourDto tourDto, BindingResult result, @RequestParam int id, Model model) {
        if (result.hasErrors()) {
            // Nếu có lỗi khi validate, quay lại trang chỉnh sửa với lỗi
            return "tours/EditTour";
        }
        
        try {
            // Lấy tour theo id
        	Tour tour = repo.findById(id).get();
        	
        	if (tour == null) {
        		// Nếu không tìm thấy tour, quay lại danh sách
        		return "redirect:/tours";
        	}

            // Cập nhật thông tin tour từ tourDto
        	tour.setName(tourDto.getName());
        	tour.setDescription(tourDto.getDescription());
            tour.setPrice(tourDto.getPrice());
            tour.setDuration(tourDto.getDuration());
            tour.setLocation(tourDto.getLocation());

            // Lưu lại thông tin tour đã cập nhật
            repo.save(tour);
        } catch (Exception ex) {
        	System.out.println("Exception: " + ex.getMessage());
        	return "redirect:/tours";  // Nếu có lỗi, quay lại danh sách tour
        }
        
        // Quay lại trang danh sách tour sau khi chỉnh sửa thành công
        return "redirect:/tours";
    }
    
    
    // Xử lý xóa tour
    @PostMapping("/delete")
    public String deleteTour(@RequestParam int id) {
        try {
            Tour tour = repo.findById(id).orElse(null);
            if (tour != null) {
                repo.delete(tour);
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return "redirect:/tours";  // Sau khi xóa, quay lại trang danh sách
    }

}
