"use client"
import React from "react";
import {logout} from "@/services/auth";
import {useRouter} from "next/navigation";

const HomeContainer: React.FC = () => {
    const router = useRouter();

    const handleLogout = async () => {
        try {
            const accessToken: string = localStorage.getItem("jwt-access-token") ?? "";
            const response = await logout(accessToken);

            if (response?.success) {
                localStorage.removeItem("jwt-access-token");
                router.push("/login");
            }
        } catch (error) {
            console.error(error);
        }
    }

    return (
        <div>
            Home Page
            <button onClick={handleLogout}>
                Logout
            </button>
        </div>
    )
}

export default HomeContainer;