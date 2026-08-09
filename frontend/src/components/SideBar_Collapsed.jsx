// components/Sidebar.jsx
import { NavLink } from 'react-router-dom'
import {useContext} from "react";
import {UserContext} from "../UserContext.jsx";
import {ROLES} from "../utils/roles.jsx";

const itens = [
    { label: 'Distribuições', icon: 'bi-justify', path: '/distribuicoes', roles: [ROLES.COORDENADOR, ROLES.DIRETOR_COORDENADOR] },
    { label: 'Comparações', icon: 'bi-arrow-down-up', path: '/comparacoes', roles: [ROLES.COORDENADOR, ROLES.DIRETOR_COORDENADOR]},
    { label: 'Docentes', icon: 'bi-person', path: '/docentes', roles: [ROLES.ADMIN] },
    { label: 'Departamentos', icon: 'bi-house-door', path: '/departamentos', roles: [ROLES.ADMIN] },
    { label: 'Cursos', icon: 'bi-mortarboard', path: '/cursos', roles: [ROLES.ADMIN] },
    { label: 'Unidades Curriculares', icon: 'bi-journals', path: '/ucs', roles: [ROLES.ADMIN] },
    { label: 'Utilizadores', icon: 'bi-people', path: '/utilizadores', roles: [ROLES.ADMIN] },

]

function Sidebar({ collapsed, onToggle }) {
    const { user } = useContext(UserContext)

    const itensVisiveis = itens.filter(item =>
        !item.roles || item.roles.some(r => user?.role?.includes(r))
    );

    return (
        <aside className={`sidebar ${collapsed ? 'collapsed' : ''}`}>

            <div className="sidebar-logo">
                <div className="icon-box">
                    <i className="bi bi-grid sidebar-icon"></i>
                </div>
                <span className="logo-text">Gestão de Serviço Docente</span>
            </div>

            <nav className="sidebar-nav">
                {itensVisiveis.map((item) => (
                    <NavLink
                        key={item.path}
                        to={item.path}
                        className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}
                    >
                        <i className={`bi ${item.icon} nav-icon`}></i>
                        <span className="nav-label">{item.label}</span>
                    </NavLink>
                ))}
            </nav>

            <button
                className="sidebar-toggle"
                onClick={onToggle}
                title={collapsed ? 'Expandir menu' : 'Recolher menu'}
            >
                <i className={`bi ${collapsed ? 'bi-layout-sidebar-reverse' : 'bi-layout-sidebar'}`}></i>
            </button>

        </aside>
    )
}

export default Sidebar