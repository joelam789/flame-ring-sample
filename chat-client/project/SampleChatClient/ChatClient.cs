using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SampleChatClient
{
    public interface ChatClient
    {
        void EnterLobby(string result);

        void UpdateRoomList(List<RoomSummary> rooms);

        void CreateRoom(string roomName, string result);

        void EnterRoom(string roomName, string result);

        void ExitRoom(string result);

        void UpdateUserList(List<String> users);

        void ReceiveChatMessage(SendMessageReply reply);

    }
}
