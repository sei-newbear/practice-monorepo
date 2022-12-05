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
	let title = "";
	let createdTask: {id: number} = {id: 0};

	async function getTasks() {
		const response = await fetch(`http://localhost:8089/v1/tasks`, {
			method: 'GET',
			headers: {
				'content-type': 'application/json'
			}
		});

		tasks = await response.json();
	}
	onMount(async () => {
		await getTasks();
	});


	async function doPost() {
		const response = await fetch(`http://localhost:8089/v1/tasks`, {
			method: 'POST',
			headers: {
				'content-type': 'application/json'
			},
			body: JSON.stringify({title})
		})

		createdTask = await response.json()
		await getTasks();
	}
</script>

<svelte:head>
	<title>Tasks</title>
</svelte:head>

<div class="tasks">
	<h1>Tasks</h1>
	<input bind:value={title} class="title">
	<button type="button" on:click={doPost} class="submit">送信</button>
	{createdTask.id}

	{#each tasks.tasks as task}
		<div class="task">
			<span class="id">{task.id}</span>: <span class="title">{task.title}</span>
		</div>
	{/each}
</div>
