package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CommentDTO;
import com.example.demo.entity.Board;
import com.example.demo.entity.Comment;
import com.example.demo.repository.CommentRepository;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepository repository;

	@Override
	public int register(CommentDTO dto) {
		Comment entity = dtoToEntity(dto);
		repository.save(entity);

		return entity.getCommentNo();
	}

//	@Override
//	public List<CommentDTO> getList() {
//		List<Comment> entityList = repository.findAll();
//		List<CommentDTO> dtoList = new ArrayList<>();
//		for (Comment entity : entityList) {
//			CommentDTO dto = entityToDto(entity);
//			dtoList.add(dto);
//		}
//
//		return dtoList;
//	}
	
	@Override
	public List<CommentDTO> getListByBoardNo(int boardNo) {
		Board board = Board.builder().no(boardNo).build();  //엔티티 생성
		List<Comment> entityList = repository.findByBoard(board);
		List<CommentDTO> dtoList = new ArrayList<>();
		for (Comment entity : entityList) {
			CommentDTO dto = entityToDto(entity);
			dtoList.add(dto);
		}

		return dtoList;
	}
	

//	@Override
//	public CommentDTO read(int no) {
//		Optional<Comment> result = repository.findById(no);
//		if(result.isPresent()) {
//			Comment entity = result.get();
//			return entityToDto(entity);
//		}
//		return null;
//	}

//	@Override
//	public void modify(CommentDTO dto) {
//		int commentNo = dto.getCommentNo();
//		Optional<Comment> result = repository.findById(commentNo);
//		if(result.isPresent()) {
//			Comment entity = result.get();
//			entity.setContent(dto.getContent());
//			repository.save(entity);
//		}
//	}

	@Override
	public boolean remove(int no) {
		//해당 댓글이 있는지 확인
		Optional<Comment> comment = repository.findById(no);
		//없다면 false 변환
		if(comment.isEmpty()) {
			return false; 
		}
		if(comment.isPresent()) {
			// 있다면 댓글 삭제 후 true 변환
			repository.deleteById(no);
			
		}
		return true;
	}	

}
