print("Hello, World!")
print("This is a test to check TeaVM compatibility.")

-- Simple function test
function add(a, b)
    return a + b
end

print("5 + 3 =", add(5, 3))

-- Table test
local t = {name = "Luaj", version = "3.0"}
print("Name:", t.name, "Version:", t.version)