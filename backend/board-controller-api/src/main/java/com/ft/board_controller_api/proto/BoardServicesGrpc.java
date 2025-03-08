package com.ft.board_controller_api.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.60.0)",
    comments = "Source: board.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class BoardServicesGrpc {

  private BoardServicesGrpc() {}

  public static final java.lang.String SERVICE_NAME = "com.ft.board_controller_api.proto.BoardServices";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.ft.board_controller_api.proto.BeatRequest,
      com.ft.board_controller_api.proto.BeatResponse> getCreateBeatMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateBeat",
      requestType = com.ft.board_controller_api.proto.BeatRequest.class,
      responseType = com.ft.board_controller_api.proto.BeatResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.ft.board_controller_api.proto.BeatRequest,
      com.ft.board_controller_api.proto.BeatResponse> getCreateBeatMethod() {
    io.grpc.MethodDescriptor<com.ft.board_controller_api.proto.BeatRequest, com.ft.board_controller_api.proto.BeatResponse> getCreateBeatMethod;
    if ((getCreateBeatMethod = BoardServicesGrpc.getCreateBeatMethod) == null) {
      synchronized (BoardServicesGrpc.class) {
        if ((getCreateBeatMethod = BoardServicesGrpc.getCreateBeatMethod) == null) {
          BoardServicesGrpc.getCreateBeatMethod = getCreateBeatMethod =
              io.grpc.MethodDescriptor.<com.ft.board_controller_api.proto.BeatRequest, com.ft.board_controller_api.proto.BeatResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateBeat"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.ft.board_controller_api.proto.BeatRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.ft.board_controller_api.proto.BeatResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BoardServicesMethodDescriptorSupplier("CreateBeat"))
              .build();
        }
      }
    }
    return getCreateBeatMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static BoardServicesStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BoardServicesStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BoardServicesStub>() {
        @java.lang.Override
        public BoardServicesStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BoardServicesStub(channel, callOptions);
        }
      };
    return BoardServicesStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static BoardServicesBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BoardServicesBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BoardServicesBlockingStub>() {
        @java.lang.Override
        public BoardServicesBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BoardServicesBlockingStub(channel, callOptions);
        }
      };
    return BoardServicesBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static BoardServicesFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BoardServicesFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BoardServicesFutureStub>() {
        @java.lang.Override
        public BoardServicesFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BoardServicesFutureStub(channel, callOptions);
        }
      };
    return BoardServicesFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void createBeat(com.ft.board_controller_api.proto.BeatRequest request,
        io.grpc.stub.StreamObserver<com.ft.board_controller_api.proto.BeatResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateBeatMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service BoardServices.
   */
  public static abstract class BoardServicesImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return BoardServicesGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service BoardServices.
   */
  public static final class BoardServicesStub
      extends io.grpc.stub.AbstractAsyncStub<BoardServicesStub> {
    private BoardServicesStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BoardServicesStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BoardServicesStub(channel, callOptions);
    }

    /**
     */
    public void createBeat(com.ft.board_controller_api.proto.BeatRequest request,
        io.grpc.stub.StreamObserver<com.ft.board_controller_api.proto.BeatResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateBeatMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service BoardServices.
   */
  public static final class BoardServicesBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<BoardServicesBlockingStub> {
    private BoardServicesBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BoardServicesBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BoardServicesBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.ft.board_controller_api.proto.BeatResponse createBeat(com.ft.board_controller_api.proto.BeatRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateBeatMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service BoardServices.
   */
  public static final class BoardServicesFutureStub
      extends io.grpc.stub.AbstractFutureStub<BoardServicesFutureStub> {
    private BoardServicesFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BoardServicesFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BoardServicesFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.ft.board_controller_api.proto.BeatResponse> createBeat(
        com.ft.board_controller_api.proto.BeatRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateBeatMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_BEAT = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CREATE_BEAT:
          serviceImpl.createBeat((com.ft.board_controller_api.proto.BeatRequest) request,
              (io.grpc.stub.StreamObserver<com.ft.board_controller_api.proto.BeatResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getCreateBeatMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.ft.board_controller_api.proto.BeatRequest,
              com.ft.board_controller_api.proto.BeatResponse>(
                service, METHODID_CREATE_BEAT)))
        .build();
  }

  private static abstract class BoardServicesBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    BoardServicesBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.ft.board_controller_api.proto.BeatsProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("BoardServices");
    }
  }

  private static final class BoardServicesFileDescriptorSupplier
      extends BoardServicesBaseDescriptorSupplier {
    BoardServicesFileDescriptorSupplier() {}
  }

  private static final class BoardServicesMethodDescriptorSupplier
      extends BoardServicesBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    BoardServicesMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (BoardServicesGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new BoardServicesFileDescriptorSupplier())
              .addMethod(getCreateBeatMethod())
              .build();
        }
      }
    }
    return result;
  }
}
