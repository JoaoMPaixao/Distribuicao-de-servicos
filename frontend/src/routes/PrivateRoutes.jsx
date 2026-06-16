import { Navigate } from "react-router-dom";

import { UserContext } from "./UserContext.jsx";
import { useContext } from "react";



export function PrivateRoutes({ children, roles }) {

    const { user } = useContext(UserContext);

    // não está logado --> redireciona para login
    if (user === null) {
        return <Navigate to="/" replace />;
    }
    else if (user === undefined){
        return null
    }


    // está logado, mas não tem permissão
    if (roles && (!user.roles || !user.roles.some(role => roles.includes(role)))) {
        return <p>Não tem acesso a esta página.</p>;
    }



    // tem permissão --> mostra a página
    return <div>{children}</div>;
}
