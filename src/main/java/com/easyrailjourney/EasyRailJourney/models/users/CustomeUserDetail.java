package com.easyrailjourney.EasyRailJourney.models.users;
// package com.easyrailjourney.EasyRailJourney.models;

// import java.util.Collection;

// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;

// import lombok.Data;



// @Data
// public class CustomeUserDetail implements UserDetails {

//     private User user;

//     public CustomeUserDetail(User user) {
//         this.user = user;
//     }

//     @Override
//     public Collection<? extends GrantedAuthority> getAuthorities() {
//         return user.getRoles()
//                 .stream()
//                 .map(role -> new SimpleGrantedAuthority(role.getName()))
//                 .toList();
//     }

//     @Override
//     public String getPassword() {
//         return user.getPassword();
//     }

//     @Override
//     public String getUsername() {
//         return user.getFullName();
//     }

//     public User getUser() {
//         return user;
//     }
// }