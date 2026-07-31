// components/Sidebar.jsx
import { NavLink } from 'react-router-dom'

const itens = [
    { label: 'Distribuições', icon: 'bi-justify', path: '/distribuicoes' },
    { label: 'Comparações', icon: 'bi-arrow-down-up', path: '/comparacoes' },
    { label: 'Docentes', icon: 'bi-person', path: '/docentes' },
    { label: 'Departamentos', icon: 'bi-house-door', path: '/departamentos' },
    { label: 'Cursos', icon: 'bi-mortarboard', path: '/cursos' },
    { label: 'Unidades Curriculares', icon: 'bi-journals', path: '/ucs' },
    { label: 'Utilizadores', icon: 'bi-people', path: '/utilizadores' },

]

function Sidebar({ collapsed, onToggle }) {
    return (
        <aside className={`sidebar ${collapsed ? 'collapsed' : ''}`}>

            <div className="sidebar-logo">
                <div className="icon-box">
                    <i className="bi bi-grid sidebar-icon"></i>
                </div>
                <span className="logo-text">Gestão de Serviço Docente</span>
            </div>

            <nav className="sidebar-nav">
                {itens.map((item) => (
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