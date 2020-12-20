package jpa.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * @author : leesangbae
 * @project : jpa
 * @since : 2020-12-15
 */
@DataJpaTest
@DisplayName("Member Repository Test Class")
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private StationRepository stationRepository;

    @Test
    @DisplayName("Member 저장하기 Test")
    void memberSaveTest() {
        final String email = "email@email.com";
        final String password = "p@ssw0rd";
        final int age = 20;

        Member member = new Member(email, password, age);
        Member savedMember = memberRepository.save(member);

        assertAll(
                () -> assertThat(savedMember.getId()).isNotNull(),
                () -> assertThat(savedMember.getEmail()).isEqualTo(email),
                () -> assertThat(savedMember.getPassword()).isEqualTo(password),
                () -> assertThat(savedMember.getAge()).isEqualTo(age)
        );
    }

    @Test
    @DisplayName("Member 이메일로 검색 Test")
    void findByEmailTest() {
        final String email = "email@email.com";
        final String password = "p@ssw0rd";
        final int age = 20;
        Member member = new Member(email, password, age);
        memberRepository.save(member);

        Member savedMember = memberRepository.findByEmail(email);

        assertThat(savedMember.getId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("Member에 등록된 Favorites 탐색 Test")
    void findByEmailWithFavorites() {
        final String email = "email@email.com";
        final String password = "p@ssw0rd";
        final String stationName = "Test역";

        Member member = memberRepository.save(new Member(email, password, 20));
        Station station = stationRepository.save(new Station(stationName));
        Favorite favorite = favoriteRepository.save(new Favorite(station, station));

        member.add(favorite);

        Member savedMember = memberRepository.findByEmailWithFavorites(email);

        List<Favorite> favorites = savedMember.getFavorites();
        assertThat(favorites.size()).isEqualTo(1);
        assertThat(favorites.get(0)).isEqualTo(favorite);
    }

    @Test
    @DisplayName("Member 생성시 에러 Test")
    void shouldBeExceptionCreateMemberTest() {

        assertAll(
                () -> assertThatThrownBy(() -> new Member(null, "password", 20))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("Member name, password는 필수 값 입니다."),
                () -> assertThatThrownBy(() -> new Member("email", null, 20))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("Member name, password는 필수 값 입니다."),
                () -> assertThatThrownBy(() -> new Member("email", "password", -1))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("Member age는 0보다 커야합니다.")
        );

    }
}
