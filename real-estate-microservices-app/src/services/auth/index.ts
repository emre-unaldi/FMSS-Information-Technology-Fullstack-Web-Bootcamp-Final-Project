interface IUserLoginData {
    username: string;
    password: string;
}

const login = async (data: IUserLoginData) => {
    try {
        const response = await fetch("http://localhost:8080/api/v1/auth/login", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
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

const logout = async (accessToken: string) => {
    try {
        const response = await fetch("http://localhost:8080/api/v1/auth/logout", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${accessToken}`
                }
            }
        );

        if (!response.ok) {
            new Error(`HTTP error! status: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        throw new Error(error instanceof Error ? error.message : String(error));
    }
}

const verifyToken = async (accessToken: string) => {
    try {
        const response = await fetch("http://localhost:8080/api/v1/auth/verifyToken", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({ "accessToken": `${accessToken}` })
            }
        );

        if (!response.ok) {
            new Error(`HTTP error! status: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        throw new Error(error instanceof Error ? error.message : String(error));
    }
}


export {login, logout, verifyToken};
export type {IUserLoginData};

