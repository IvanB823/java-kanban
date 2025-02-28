package httpServer.adaptersanddeserializer;

import com.google.gson.*;
import httpServer.HttpTaskServer;
import taskstypes.Epic;
import taskstypes.SubTask;
import taskstypes.Task;

import java.lang.reflect.Type;


public class AllTasksJsonDeserializer implements JsonDeserializer<Task> {
    @Override
    public Task deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String taskType = jsonObject.get("type").getAsString().toUpperCase();

        switch (taskType) {
            case "TASK":
                return HttpTaskServer.getRegularGson().fromJson(jsonObject, Task.class);
            case "EPIC":
                return HttpTaskServer.getRegularGson().fromJson(jsonObject, Epic.class);
            case "SUBTASK":
                return HttpTaskServer.getRegularGson().fromJson(jsonObject, SubTask.class);
            default:
                throw new JsonParseException("Неизвестный тип задачи: " + taskType);
        }
    }
}
