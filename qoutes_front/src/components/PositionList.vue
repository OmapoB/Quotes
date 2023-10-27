<template>
    <div @click="test">
        Ping
    </div>
    <div>
        <h3>Позиции</h3>
        <position-row
            v-for="position in positions"
            :position="position"
            :key="position.uid"
        />
    </div>
</template>

<script>
import PositionRow from "@/components/PositionRow";
import axios from "axios";

export default {
    name: "PositionList",
    components: {PositionRow},
    props: {
        positions: {
            type: Array,
            required: true,
        }
    },
    data() {
        return {
            connection: null
        }
    },
    created() {
        this.onClose(null)
        this.connect()
    },
    methods: {
        connect() {
            this.connection = new WebSocket("ws://localhost:8080/main")
            this.connection.onopen = this.onOpen
            this.connection.onmessage = this.onMessage
            this.connection.onclose = this.onClose
        },
        onOpen(event) {
            console.log("Connected")

        },
        onMessage(event) {
            const message = JSON.parse(event.data)
            this.positions[this.positions.findIndex(pos => pos.uid === message.uid)]
                .nominal = message.nominal
        },
        onClose(event) {
            console.log("Connection closed")
            axios.post("http://localhost:8080/positions/close")
        },
        test() {
            axios.post("http://localhost:8080/positions/subscribe",
                this.positions.map(s => s.uid))
            console.log(Array.isArray(this.positions.map(s => s.uid)))
        }
    }
}
</script>

<style scoped>

</style>