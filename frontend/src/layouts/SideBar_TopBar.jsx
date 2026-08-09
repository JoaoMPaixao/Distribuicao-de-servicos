import Sidebar from '../components/Sidebar'
import Topbar from '../components/Topbar'


function SideBar_TopBar({ children}) {
    return (
        <>
            <Sidebar />
            <Topbar />
            <main className="main">
                <div className="main-content">
                    {children}
                </div>
            </main>
        </>
    )
}

export default SideBar_TopBar