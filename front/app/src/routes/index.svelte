<script lang="ts">
    type Task = {
        id: number,
        title: string
    }
    type Tasks = {
        total: number,
        tasks: Task[]
    }

    export let tasks: Tasks = {total: 2, tasks: [{id: 1, title: "test1"}]}
</script>

<svelte:head>
    <title>Tasks</title>
</svelte:head>

<div class="tasks">
    <h1>Tasks</h1>

    {#each tasks.tasks as task}
        <div class="task">
            {task.id}: {task.title}
        </div>
    {/each}
</div>