// layouts/SideBarCollapsed_TopBar.jsx
import { useState } from 'react'
import SideBar_Collapsed from '../components/SideBar_Collapsed.jsx'
import Topbar from '../components/Topbar.jsx'
import TopBar_Collapsed from "../components/TopBar_Collapsed.jsx";
import TopBar from "../components/Topbar.jsx";

function SideBarCollapsed_TopBar({ children, breadcrumb }) {
    const [collapsed, setCollapsed] = useState(false)

    return (
        <>
            <SideBar_Collapsed
                collapsed={collapsed}
                onToggle={() => setCollapsed(!collapsed)}
            />

            <TopBar_Collapsed
                collapsed={collapsed}
                breadcrumb={breadcrumb}
            />

            <main className={`main-detalhe ${collapsed ? 'collapsed' : ''}`}>
                {children}
            </main>
        </>
    )
}

export default SideBarCollapsed_TopBar