import { Navigate } from "react-router-dom";
import { UserContext } from "../UserContext.jsx";
import { useContext } from "react";

export function PrivateRoutes({ children, roles }) {
    const { user } = useContext(UserContext);

    // Não carregou o user ainda
    if (user === undefined) {
        return null;
    }

    // Não está logado --> redireciona para o login
    if (user === null) {
        return <Navigate to="/" replace />;
    }

    // Validação corrigida:
    // user.role é um Array (ex: ['ADMIN']). Verificamos se alguma role do user está no array de roles da rota.
    const hasPermission = !roles || (
        Array.isArray(user.role) && user.role.some(r => roles.includes(r))
    );

    if (!hasPermission) {
        return <Navigate to="/semAcesso" replace />;
    }

    // Tem permissão --> mostra a página
    return children;
}