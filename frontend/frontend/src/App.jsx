import { Navigate, Route, Routes } from "react-router-dom";

import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import Properties from "./pages/Properties";
import Leases from "./pages/Leases";
import Payments from "./pages/Payments";
import Maintenance from "./pages/Maintenance";

function ProtectedRoute({ children }) {
  const token = localStorage.getItem("token");

  if (!token) {
    return <Navigate to="/" replace />;
  }

  return children;
}

function Protected({ children }) {
  return (
    <ProtectedRoute>
      {children}
    </ProtectedRoute>
  );
}

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/register" element={<Register />} />

      <Route
        path="/dashboard"
        element={<Protected><Dashboard /></Protected>}
      />

      <Route
        path="/properties"
        element={<Protected><Properties /></Protected>}
      />

      <Route
        path="/leases"
        element={<Protected><Leases /></Protected>}
      />

      <Route
        path="/payments"
        element={<Protected><Payments /></Protected>}
      />

      <Route
        path="/maintenance"
        element={<Protected><Maintenance /></Protected>}
      />

      <Route
        path="*"
        element={<Navigate to="/dashboard" replace />}
      />
    </Routes>
  );
}
