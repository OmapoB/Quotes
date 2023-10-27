<template>
    <form @submit.prevent>
        <input class="my-input"
               v-model="searchQuery"
               type="text"
               placeholder="Поиск">
        <button class="btn"
                @click="sendQuery">
            Найти
        </button>
    </form>
</template>

<script>
export default {
    name: "PositionSearch",
    data() {
        return {
             searchQuery: ""
        }
    },
    methods: {
        sendQuery() {
            this.$emit("find", this.searchQuery)
            this.searchQuery = ""
        }
    }
}
</script>

<style scoped>

form {
    display: flex;
    flex-direction: row;
}

.my-input {
    width: 50%;
    border: 1px solid cornflowerblue;
    padding: 10px 15px;
    margin-top: 10px;
}

.btn {
    margin-top: 10px;
    margin-left: 10px;
    padding: 10px 15px;
    background: none;
    color: cornflowerblue;
    border: 1px solid cornflowerblue;
}
</style>