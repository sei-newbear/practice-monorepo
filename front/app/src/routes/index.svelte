<script lang="ts">
	import { onMount } from 'svelte';

	type Task = {
		id: number;
		title: string;
	};
	type Tasks = {
		total: number;
		tasks: Task[];
	};

	export let tasks: Tasks = {total:0, tasks: []};

	onMount(async () => {
		const response = fetch(`http://localhost:8089/tasks`, {
			method: 'GET',
			headers: {
				'content-type': 'application/json'
			}
		});

		tasks = await (await response).json();
	});
</script>

<svelte:head>
	<title>Tasks</title>
</svelte:head>

<div class="tasks">
	<h1>Tasks</h1>

	{#each tasks.tasks as task}
		<div class="task">
			<span class="id">{task.id}</span>: <span class="title">{task.title}</span>
		</div>
	{/each}
</div>
