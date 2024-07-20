"use client"
import React from "react";
import {logout} from "@/services/auth";
import {useRouter} from "next/navigation";
import Cookies from "universal-cookie";

const HomeContainer: React.FC = () => {
    const router = useRouter();
    const cookies = new Cookies();

    const handleLogout = async () => {
        try {
            const accessToken: string = cookies.get("jwt-access-token");
            const response = await logout(accessToken);

            if (response?.success) {
                cookies.remove("jwt-access-token");
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