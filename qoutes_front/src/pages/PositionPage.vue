<template>
    <div>
        <position-search
            @find="findPositions"
        />
        <position-list
            :positions="positions"
        />
        <div class="page__wrapper">
            <div
                v-for="pageNumber in totalPages"
                class="page"
                :class="{
                'current-page': page === pageNumber
                }"
                :key="pageNumber"
                @click="this.page = pageNumber; getPosts()"
            >
                {{ pageNumber }}
            </div>
        </div>
    </div>
</template>

<script>
import PositionSearch from "@/components/PositionSearch";
import PositionList from "@/components/PositionList";
import axios from "axios";

const position_service = "http://localhost:8080/positions"

export default {
    components: {
        PositionSearch, PositionList
    },
    name: "PositionPage",
    data() {
        return {
            positions: [],
            page: 1,
            limit: 10,
            totalPages: 0
        }
    },
    created() {
        this.getPosts()
    },
    methods: {
        async findPositions(searchQuery) {
            if (searchQuery !== "") {
                const response = await axios.get(position_service.concat("/tickers/" + searchQuery.toUpperCase()))
                this.positions = response.data
            }
        },
        async getPosts() {
            const response = await axios.get(position_service.concat("/get_all"),
                {
                    params: {
                        p: this.page,
                        els: this.limit
                    }
                })
            this.positions = response.data.content
            this.totalPages = response.data.totalPages
        }
    }
}
</script>

<style scoped>


.page__wrapper {
    display: flex;
    margin-top: 15px;
}

.page {
    border: 1px solid lightblue;
    padding: 10px;
}

.current-page {
    border: 2px solid cornflowerblue;
}
</style>