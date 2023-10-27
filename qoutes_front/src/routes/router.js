import Main from "@/pages/Main";
import {createRouter, createWebHistory} from "vue-router";
import PositionPage from "@/pages/PositionPage";
import PositionItemPage from "@/components/PositionItemPage";

const routes = [
    {
        path: "/",
        component: Main
    },
    {
        path: "/positions",
        component: PositionPage
    },
    {
        path: "/positions/:id",
        name: "PositionPage",
        component: PositionItemPage,
    }
]

const router = createRouter({
    routes,
    history: createWebHistory()
})

export default router