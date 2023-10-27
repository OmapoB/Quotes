<template>
    <div class="position-row"
         @click="openItem">
        <p class="my-p"> {{ position.name }} </p>
        <p class="my-p"> {{ position.ticker }} </p>
        <p class="my-p"> {{ position.nominal }} </p>
    </div>
</template>

<script>
export default {
    name: "PositionRow",
    props: {
        position: {
            type: Object,
            required: true
        }
    },
    methods: {
        openItem() {
            this.$router.push({
                name: "PositionPage",
                params: {
                    id: this.position.uid,
                }
            })
        }
    }
}
</script>

<style scoped>
.position-row {
    display: flex;
    padding: 15px;
    border: 1px solid cornflowerblue;
    margin: 5px;
}

.my-p {
    flex-direction: row;
    flex-basis: 35%;
}
</style>