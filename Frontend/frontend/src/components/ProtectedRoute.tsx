"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

type Props = {
  children: React.ReactNode;
  requiredRole?: "ADMIN" | "USER"; // 👈 nuevo prop opcional
};

export default function ProtectedRoute({ children, requiredRole }: Props) {
  const router = useRouter();
  const [authorized, setAuthorized] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem("token");
    const role = localStorage.getItem("role");

    if (!token) {
      router.replace("/login");
      return;
    }

    if (requiredRole && role !== requiredRole) {
      router.replace("/unauthorized");
      return;
    }

    setAuthorized(true);
  }, [router, requiredRole]);

  if (!authorized) {
    return (
      <div className="flex justify-center items-center h-screen">
        <p className="text-gray-600 text-lg">Verificando sesión...</p>
      </div>
    );
  }

  return <>{children}</>;
}
