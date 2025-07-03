import ProtectedRoute from "@/components/ProtectedRoute";
import NewUserForm from "@/components/NewUserForm";

export default function NewUserPage() {
  return (
    <ProtectedRoute requiredRole="ADMIN">
      <NewUserForm />
    </ProtectedRoute>
  );
}
