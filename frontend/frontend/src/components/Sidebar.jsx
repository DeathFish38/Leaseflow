// src/components/Sidebar.jsx

import { NavLink, useNavigate } from "react-router-dom";

export default function Sidebar() {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  return (
    <aside className="sidebar">
      <div className="sidebar-logo">
        <h2>Leaseflow</h2>
      </div>

      <nav className="sidebar-nav">
        <NavLink
          to="/dashboard"
          className={({ isActive }) =>
            isActive ? "sidebar-link active" : "sidebar-link"
          }
        >
          Dashboard
        </NavLink>

        <NavLink
          to="/properties"
          className={({ isActive }) =>
            isActive ? "sidebar-link active" : "sidebar-link"
          }
        >
          Properties
        </NavLink>

        <NavLink
          to="/leases"
          className={({ isActive }) =>
            isActive ? "sidebar-link active" : "sidebar-link"
          }
        >
          Leases
        </NavLink>

        <NavLink
          to="/payments"
          className={({ isActive }) =>
            isActive ? "sidebar-link active" : "sidebar-link"
          }
        >
          Payments
        </NavLink>
      </nav>

      <div className="sidebar-bottom">
        <button
          className="sidebar-logout"
          onClick={handleLogout}
        >
          Logout
        </button>
      </div>
    </aside>
  );
}