import ProtectedRoute from "@/components/ProtectedRoute";
import RenewLicenseForm from "@/components/RenewLicenseForm";

export default function RenewLicensePage() {
  return (
    <ProtectedRoute>
      <RenewLicenseForm />
    </ProtectedRoute>
  );
}
