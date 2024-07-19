import {verifyJwtToken} from "@/libs/auth";
import {NextRequest, NextResponse} from "next/server";

const AUTH_PAGES = ["/login", "/register"];

const isAuthPages = (url: string) => AUTH_PAGES.some(page => page.startsWith(url));

const middleware = async (request: NextRequest) => {
/*
    const { url, nextUrl, cookies } = request;
    const { value: accessToken } = cookies.get("jwt-access-token") ?? { value: null }

    const hasVerifiedToken = accessToken && (await verifyJwtToken(accessToken));
    const isAuthPageRequested = isAuthPages(nextUrl.pathname);

    console.log("hasVerifiedToken : " + hasVerifiedToken);

    if (isAuthPageRequested) {
        if (!hasVerifiedToken) {
            const response = NextResponse.next();
            response.cookies.delete("jwt-access-token");
            response.cookies.delete("jwt-refresh-token");

            return response;
        }

        return NextResponse.redirect(new URL("/", url));
    }

    if(!hasVerifiedToken) {
        const searchParams = new URLSearchParams(nextUrl.searchParams);
        searchParams.set("next", nextUrl.pathname);

        const response = NextResponse.redirect(new URL(`/login?${searchParams}`, url));
        response.cookies.delete("jwt-access-token");
        response.cookies.delete("jwt-refresh-token");

        return response;
    }

*/
    return NextResponse.next();
}

export const config = { matcher: ["/login", "/home/:path*"] };

export default middleware;