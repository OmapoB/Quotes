import {createApp} from 'vue'
import App from "@/App";
import router from "@/routes/router";

createApp(App)
    .use(router)
    .mount('#app')
