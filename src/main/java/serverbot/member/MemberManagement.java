package serverbot.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberManagement {

    @Autowired
    MemberRepository memberRepository;

    public Streamable<Member> findAll() {
        return memberRepository.findAll();
    }
}
