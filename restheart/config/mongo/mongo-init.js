rs.initiate({
    _id: 'rs1',
    members: [{
        _id: 0, host: 'mongodb:27017'
    }]
});

db.createRole({
    role: "blogsManager",
    privileges: [
        {
            resource: {
                db: "storage",
                collection: "blogs"
            },
            actions: ["find", "insert", "update", "remove"]
        }
    ],
    roles: []
});


db.createUser({
    "user": "admin",
    "pwd": "admin",
    "roles": [{"role": "blogsManager", "db": "storage"}],
    "mechanisms": ["SCRAM-SHA-1"]
});
