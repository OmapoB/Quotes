<template>
    <div
        v-for="field in position"
    >
        {{ field }}
    </div>
</template>

<script>
import axios from "axios";

const position_service = "http://localhost:8080/positions/"
export default {
    name: "PositionItemPage",
    data() {
        return {
            position: null
        }
    },
    created() {
        this.getByUid()
    },
    methods: {
        async getByUid() {
            const response = await axios.get(position_service.concat(this.$route.params.id))
            this.position = response.data
        }
    }
}
</script>

<style scoped>

</style>