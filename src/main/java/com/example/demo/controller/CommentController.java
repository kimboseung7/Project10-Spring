package com.example.demo.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.CommentDTO;
import com.example.demo.service.CommentService;

@Controller
@RequestMapping("/comment")
public class CommentController {
	
	@Autowired
	CommentService service;

	//게시물별 댓글 목록 조회
	@ResponseBody
	@GetMapping("/list")
	public List<CommentDTO> list(@RequestParam(name = "boardNo") int boardNo) {
		List<CommentDTO> commentlist = service.getListByBoardNo(boardNo);

		return commentlist;
	}
	
	@ResponseBody
	@PostMapping("/register")
	public HashMap<String,Boolean> register(CommentDTO dto, Principal principal) {
		
		
		//맵 객체 생성 젝슨이 제이슨 형태로 반환
		HashMap<String, Boolean> map = new HashMap<>();
		
		String id = principal.getName();
		dto.setWriter(id);
		
		
		
		// 새로운 댓글 등록
		service.register(dto);
		// 처리결과 반환
		map.put("success", true);
		return map;
	}
	
	@ResponseBody//어노테이션을 사용해서 json 형식으로 변환
	@GetMapping("/remove")
	public HashMap<String,Boolean> remove(@RequestParam(name = "commentNo") int commentNo) {
		// 맵 객체 생성
		HashMap<String,Boolean> map = new HashMap<>();
		// 댓글 삭제
		boolean result =  service.remove(commentNo);
		// 처리결과 변환
		map.put("success", result);
		
		return map;
	}

}
