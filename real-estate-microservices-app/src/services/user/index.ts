interface IUserRegisterData {
    "firstName": "string",
    "lastName": "string",
    "username": "string",
    "email": "string",
    "password": "string",
    "phoneNumber": "string",
    roles: string[]
}

const register = async (data: IUserRegisterData) => {
    try {
        const response = await fetch("http://localhost:8080/api/v1/users/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data),
            credentials: "include"
        });

        if (!response.ok) {
            new Error(`HTTP error! status: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        throw new Error(error instanceof Error ? error.message : String(error));
    }
}

export { register };
export type { IUserRegisterData };