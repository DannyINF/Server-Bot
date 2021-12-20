package serverbot.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import serverbot.role.Role;
import serverbot.role.RoleManagement;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberManagement {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RoleManagement roleManagement;

    public Streamable<Member> findAll() {
        return memberRepository.findAll();
    }

    public Optional<Member> findById(MemberId id) {
        return memberRepository.findById(id);
    }

    public void save(Member member) {
        memberRepository.save(member);
    }

    public void addRoleToMember(MemberId id, String roleId) {
        Member member = memberRepository.findById(id).get();
        List<String> roles = member.getRoles().stream().map(Role::getRoleId).collect(Collectors.toList());
        if (!roles.contains(roleId)) {
            roles.add(roleId);
        } else {
            return;
        }
        member.setRoles(roles.stream().map(role -> roleManagement.findByRoleId(role).get()).collect(Collectors.toList()));
        memberRepository.save(member);
    }

    public void setIsExiled(MemberId id, boolean isExiled) {
        Member member = memberRepository.findById(id).get();
        member.setExiled(isExiled);
        memberRepository.save(member);
    }

    public void setIsBanned(MemberId id, boolean isBanned) {
        Member member = memberRepository.findById(id).get();
        member.setBanned(isBanned);
        memberRepository.save(member);
    }
}
