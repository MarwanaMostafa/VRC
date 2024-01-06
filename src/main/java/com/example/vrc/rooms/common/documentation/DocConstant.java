package com.example.vrc.rooms.common.documentation;

public interface DocConstant {
    interface RoomConstants {
        String API_NAME = "Room";
        String API_DESCRIPTION = "Manage Room Operation";
        String API_POST_CREATE_VALUES = "Create New Room.";
        String API_POST_CREATE_DESCRIPTION = "Create New Room.";
        String API_PATCH_ROOMID_UPDATE_VALUES = "Update Room.";
        String API_PATCH_ROOMID_UPDATE_DESCRIPTION = "Update Status for specific room using room id.";

        String API_GET_ROOMS_VALUES = "Get rooms.";
        String API_GET_ROOMS_DESCRIPTION = "Get all rooms for user.";

        String API_GET_ROOM_ID_VALUES = "Get room.";
        String API_GET_ROOM_ID_DESCRIPTION = "Get specific room using national id .";

    }
}