package com.example.backend.Security;



import com.example.backend.Entity.Utilizador;
import com.example.backend.Repository.UtilizadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class CustomAuthenticationManager implements AuthenticationManager {

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private UtilizadorRepository utilizadorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) {

        Utilizador user = utilizadorRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new BadCredentialsException("Wrong email"));
        System.out.println("Tentando autenticar: " + authentication.getName());
        System.out.println("Palavra-passe fornecida: " + authentication.getCredentials());
        System.out.println("Palavra-passe hash da base de dados: " + user.getPassword());
        if (!passwordEncoder.matches(
                authentication.getCredentials().toString(),
                user.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }

        CustomUserDetails userDetails = new CustomUserDetails(user);


        return new UsernamePasswordAuthenticationToken(
                userDetails, // <-- agora é UserDetails
                null,
                userDetails.getAuthorities()
        );



        /*
        return new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                null,
                List.of(new SimpleGrantedAuthority(
                        user.getRole().getNome().name()
                ))
        );*/
    }
}


/*
@Component
public class CustomAuthenticationManager implements AuthenticationManager {

    @Autowired
    UtilizadorRepository UtilizadorRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<Utilizador> user = UtilizadorRepo.findByEmail(authentication.getName());
        if (user.isPresent()) {
            if (passwordEncoder.matches(authentication.getCredentials().toString(), user.get().getPassword())) {
                List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
                /*
                TODO quando o user tiver roles no modelo de dados deve descomentar este código e adaptar para criar os SimpleGrantedAuthority
                TODO que serão os roles correspondentes às autorizações concedidas ao user
                for (Role role : user.get().getRoleSet()) {
                    grantedAuthorityList.add(new SimpleGrantedAuthority(role.getName()));
                }*//*
                return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), grantedAuthorityList);
            } else {
                throw new BadCredentialsException("Wrong Password");
            }
        } else {
            throw new BadCredentialsException("Wrong UserName");
        }
    }
}
*/