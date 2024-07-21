interface IAddress {
    neighborhood: string,
    street: string,
    province: string,
    county: string,
    zipCode: string
}

const createAddress = async (data: IAddress, accessToken: string) => {
    try {
        const response = await fetch("http://localhost:8080/api/v1/addresses", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${accessToken}`
            },
            body: JSON.stringify(data),
            credentials: "include"
        })

        if (!response.ok) {
            new Error(`HTTP error! status: ${response.status}`);
        }

        return await response.json();
    }  catch (error) {
        throw new Error(error instanceof Error ? error.message : String(error));
    }
}

export { createAddress }
export type { IAddress }
