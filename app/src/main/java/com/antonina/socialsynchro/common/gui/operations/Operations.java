package com.antonina.socialsynchro.common.gui.operations;

public class Operations {
    private static final OperationFactory[] factories = new OperationFactory[OperationID.values().length];
    private static boolean initialized = false;

    private static void init() {
        factories[OperationID.EDIT.ordinal()] = new EditOperationFactory();
        factories[OperationID.SYNCHRONIZE.ordinal()] = new SynchronizeOperationFactory();
        factories[OperationID.STATISTICS.ordinal()] = new StatisticsOperationFactory();
        factories[OperationID.PUBLISH.ordinal()] = new PublishOperationFactory();
        factories[OperationID.UNPUBLISH.ordinal()] = new UnpublishOperationFactory();
        factories[OperationID.DELETE.ordinal()] = new DeleteOperationFactory();
        factories[OperationID.LINK.ordinal()] = new LinkOperationFactory();
        factories[OperationID.ADD_CHILD.ordinal()] = new AddChildOperationFactory();
        factories[OperationID.SAVE.ordinal()] = new SaveOperationFactory();
        factories[OperationID.ADD_ATTACHMENT.ordinal()] = new AddAttachmentOperationFactory();
        factories[OperationID.LOCK.ordinal()] = new LockOperationFactory();
        factories[OperationID.UNLOCK.ordinal()] = new UnlockOperationFactory();
        initialized = true;
    }

    public static Operation createOperation(OperationID operationID) {
        if (!initialized)
            init();
        return factories[operationID.ordinal()].createOperation();
    }

    private static abstract class OperationFactory {
        public abstract Operation createOperation();
    }

    private static class EditOperationFactory extends OperationFactory {
        @Override
        public Operation createOperation() {
            return new EditOperation();
        }
    }

    private static class SynchronizeOperationFactory extends OperationFactory {
        @Override
        public Operation createOperation() {
            return new SynchronizeOperation();
        }
    }

    private static class StatisticsOperationFactory extends OperationFactory {
        @Override
        public Operation createOperation() {
            return new StatisticsOperation();
        }
    }

    private static class PublishOperationFactory extends OperationFactory {
        @Override
        public Operation createOperation() {
            return new PublishOperation();
        }
    }

    private static class UnpublishOperationFactory extends OperationFactory {
        @Override
        public Operation createOperation() {
            return new UnpublishOperation();
        }
    }

    private static class DeleteOperationFactory extends OperationFactory {
        @Override
        public Operation createOperation() {
            return new DeleteOperation();
        }
    }

    private static class LinkOperationFactory extends OperationFactory {
        @Override
        public Operation createOperation() {
            return new LinkOperation();
        }
    }

    private static class AddChildOperationFactory extends OperationFactory {
        @Override
        public Operation createOperation() {
            return new AddChildOperation();
        }
    }

    private static class SaveOperationFactory extends OperationFactory {
        @Override
        public Operation createOperation() {
            return new SaveOperation();
        }
    }

    private static class AddAttachmentOperationFactory extends OperationFactory {
        @Override
        public Operation createOperation() {
            return new AddAttachmentOperation();
        }
    }

    private static class LockOperationFactory extends OperationFactory {
        @Override
        public Operation createOperation() {
            return new LockOperation();
        }
    }

    private static class UnlockOperationFactory extends OperationFactory {
        @Override
        public Operation createOperation() {
            return new UnlockOperation();
        }
    }
}
