import LicenseForm from "@/components/LicenseForm";
import ProtectedRoute from "@/components/ProtectedRoute";

export default function LicensePage() {
  return (
    <ProtectedRoute>
      <LicenseForm />
    </ProtectedRoute>
  );
}
