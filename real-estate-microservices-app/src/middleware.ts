import {NextRequest, NextResponse} from "next/server";
import {verifyJwtToken} from "@/libs/auth";

const AUTH_PAGES = ["/login", "/register"];

const isAuthPages = (url: string) => AUTH_PAGES.some(page => page.startsWith(url));

const middleware = async (request: NextRequest) => {
    const { url, nextUrl, cookies } = request;
    const { value: accessToken } = cookies.get("jwt-access-token") ?? { value: null }
    console.log("accessToken : " + accessToken);

    const hasVerifiedToken = accessToken && (await verifyJwtToken(accessToken));
    const isAuthPageRequested = isAuthPages(nextUrl.pathname);

    console.log("hasVerifiedToken : " + hasVerifiedToken);

    if (isAuthPageRequested) {
        if (!hasVerifiedToken?.success) {
            const response = NextResponse.next();
            response.cookies.delete("jwt-access-token");

            return response;
        }

        return NextResponse.redirect(new URL("/home", url));
    }

    if(!hasVerifiedToken?.success) {
        const searchParams = new URLSearchParams(nextUrl.searchParams);
        searchParams.set("next", nextUrl.pathname);

        const response = NextResponse.redirect(new URL(`/login?${searchParams}`, url));
        response.cookies.delete("jwt-access-token");

        return response;
    }

    return NextResponse.next();
}

export const config = { matcher: ["/login", "/home/:path*"] };

export default middleware;