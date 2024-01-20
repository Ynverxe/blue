rootProject.name = "blue"
include("event")
include("collections")
include("storage")
include("storage:storage-sql")
findProject(":storage:storage-sql")?.name = "storage-sql"
