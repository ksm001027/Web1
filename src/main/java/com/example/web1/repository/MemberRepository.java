package com.example.web1.repository;

import com.example.web1.model.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // 첫번째 인자 : 어떤 Entity인지, 두번째 인자 : pk 어떤 타입인지
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
  Optional<MemberEntity> findByMemberEmail(String memberEmail);
  Optional<MemberEntity> findByMemberName(String memberName); // 회원 이름으로 검색하는 메서드 추가
}
