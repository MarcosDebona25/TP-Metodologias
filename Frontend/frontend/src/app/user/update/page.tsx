import ProtectedRoute from "@/components/ProtectedRoute";
import UpdateUserForm from "@/components/UpdateUserForm";

export default function NewUserPage() {
  return (
    <ProtectedRoute requiredRole="ADMIN">
      <UpdateUserForm />
    </ProtectedRoute>
  );
}
