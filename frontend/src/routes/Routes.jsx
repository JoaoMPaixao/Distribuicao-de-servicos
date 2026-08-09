import { Routes, Route, Navigate } from "react-router";
import { DistribuicoesPage } from "../pages/DistribuicoesPage";
import LoginPage from "../pages/LoginPage.jsx";
import {CursosPage} from "../pages/CursosPage.jsx";
import {DepartamentosPage} from "../pages/DepartamentosPage.jsx";
import {UtilizadoresPage} from "../pages/UtilizadoresPage.jsx";
import {UcsPage} from "../pages/UcsPage.jsx";
import DistribuicaoPage from "../pages/DistribuicaoPage.jsx";
import '../styles/global.css'
import {DocentesPage} from "../pages/DocentesPage.jsx";
import ComparacaoPage from "../pages/ComparacaoPage.jsx";
import SemAcessoPage from "../pages/SemAcessoPage.jsx";
import {PrivateRoutes} from "./PrivateRoutes.jsx";
import {ROLES} from "../utils/roles.jsx";

export const AppRoutes = () => {
    return (
        <Routes>

            <Route path="/" element={<LoginPage/>} />

            <Route path="/semAcesso" element={<SemAcessoPage/>} />

            <Route path="/distribuicoes" element=
                {<PrivateRoutes roles={[ROLES.COORDENADOR, ROLES.DIRETOR_COORDENADOR]}>
                    <DistribuicoesPage />
                </PrivateRoutes>
            } />

            <Route path="/cursos" element={
                <PrivateRoutes roles={[ROLES.ADMIN]}>
                    <CursosPage />
                </PrivateRoutes>
            } />

            <Route path="/departamentos" element={
                <PrivateRoutes roles={[ROLES.ADMIN]}>
                    <DepartamentosPage />
                </PrivateRoutes>
            } />

            <Route path="/utilizadores" element={
                <PrivateRoutes roles={[ROLES.ADMIN]}>
                    <UtilizadoresPage />
                </PrivateRoutes>
            } />

            <Route path="/ucs" element={
                <PrivateRoutes roles={[ROLES.ADMIN]}>
                    <UcsPage />
                </PrivateRoutes>
            } />

            <Route path="/distribuicoes/:id/atribuicoes" element={
                <PrivateRoutes roles={[ROLES.COORDENADOR, ROLES.DIRETOR_COORDENADOR]}>
                    <DistribuicaoPage />
                </PrivateRoutes>
            } />

            <Route path="/docentes" element={
                <PrivateRoutes roles={[ROLES.ADMIN]}>
                    <DocentesPage />
                </PrivateRoutes>
            } />

            <Route path="/comparacoes" element={
                <PrivateRoutes roles={[ROLES.COORDENADOR, ROLES.DIRETOR_COORDENADOR]}>
                    <ComparacaoPage />
                </PrivateRoutes>
            } />

        </Routes>
    );
};