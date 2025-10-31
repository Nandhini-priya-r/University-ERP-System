package org.hibernate.controller;

import org.hibernate.entity.Member;
import org.hibernate.service.MemberService;
import org.hibernate.util.InputUtil;
import java.util.List;

public class MemberController {
    private final MemberService service = new MemberService();

    public void addMember() {
        String name = InputUtil.nextLine("Enter Member Name: ");
        String role = InputUtil.nextLine("Enter Role (student/faculty): ");
        service.create(new Member(name, role));
        System.out.println("✅ Member added successfully!");
    }

    public void listMembers() {
        List<Member> members = service.list();
        if (members.isEmpty()) {
            System.out.println("👤 No members found.");
            return;
        }
        members.forEach(System.out::println);
    }

    public Member get(int id) {
        return service.get(id);
    }

    public void delete(int id) {
        service.delete(id);
    }
}
