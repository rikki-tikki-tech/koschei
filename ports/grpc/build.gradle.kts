// todo: maybe use variants / configurations to do both stub & stub-lite here

// Note: We use the java-library plugin to get the protos into the artifact for this subproject
// because there doesn't seem to be an better way.
plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.protobuf)
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":domain"))
    implementation(project(":application"))

    api(libs.grpc.stub)
    api(libs.grpc.kotlin.stub)
    api(libs.grpc.protobuf)
    api(libs.protobuf.kotlin)
    api(libs.protobuf.java.util)

    api(libs.proto.google.common.protos)
}

protobuf {
    protoc {
        artifact = libs.protoc.asProvider().get().toString()
    }
    plugins {
        create("grpc") {
            artifact = libs.protoc.gen.grpc.java.get().toString()
        }
        create("grpckt") {
            artifact = libs.protoc.gen.grpc.kotlin.get().toString() + ":jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                create("grpc")
                create("grpckt")
            }
            it.builtins {
                create("kotlin")
            }
        }
    }
}
