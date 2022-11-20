import type { RequestHandler } from "@sveltejs/kit";


export const get: RequestHandler =async () => {
    const response = fetch(`http://localhost:8089/tasks`, {
        method: "GET",
        headers: {
            'content-type': 'application/json'
        }
    })

    return {
        body: {
            tasks: await (await response).json()
        }
    }

}