package com.ft.board_controller_api.service;

import com.ft.board_controller_api.proto.BeatRequest;
import com.ft.board_controller_api.proto.BeatResponse;
import com.ft.board_controller_api.proto.BoardServicesGrpc;

import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class BoardServicesGrpcImpl extends BoardServicesGrpc.BoardServicesImplBase {

    @Override
    public void createBeat(BeatRequest request,
            io.grpc.stub.StreamObserver<BeatResponse> responseObserver) {

        String boardId = request.getBoardId();
        System.out.println("Board ID: " + boardId);
        BeatResponse response = BeatResponse.newBuilder()
                // Configure os campos da resposta conforme necessário
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
