package pt.utl.ist.fenix.tools.tree;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class TreeUtilities {

	private TreeUtilities() {
		super();
	}

	public static TreeUtilities createTreeUtilities() {
		return new TreeUtilities();
	}

	private ArrayList<Object> pathRecurseComponents = new ArrayList<Object>();

	public void recurseTreeCallMethod(Object rootOfTree, Method mGetChildsMethod, Object callMethodTarget,
			Method mCallMethodAtLevel, Object[] additionalParametersToTargetMethod) throws TreeRecurseException {

		if (pathRecurseComponents.contains(rootOfTree)) {
			throw new TreeRecurseException("infinite.loop.detected", pathRecurseComponents, null);
		}

		try {

			pathRecurseComponents.add(rootOfTree);
			Object callTarget = callMethodTarget == null ? rootOfTree : callMethodTarget;

			Object[] argsAtLevel =
					new Object[(callMethodTarget == null ? 0 : 1)
							+ (additionalParametersToTargetMethod == null ? 0 : additionalParametersToTargetMethod.length)];

			if (callMethodTarget != null) {
				argsAtLevel[0] = rootOfTree;
			}

			if (additionalParametersToTargetMethod != null) {
				System.arraycopy(additionalParametersToTargetMethod, 0, argsAtLevel, (callMethodTarget == null ? 0 : 1),
						additionalParametersToTargetMethod.length);
			}

			mCallMethodAtLevel.invoke(callTarget, argsAtLevel);

			Object oChilds = mGetChildsMethod.invoke(rootOfTree);
			if (oChilds != null) {
				if (oChilds instanceof Iterable) {
					Iterable<? extends Object> iterableChilds = (Iterable<? extends Object>) oChilds;
					for (Object child : iterableChilds) {
						recurseTreeCallMethod(child, mGetChildsMethod, callMethodTarget, mCallMethodAtLevel,
								additionalParametersToTargetMethod);
					}
				} else if (oChilds instanceof Iterator) {
					Iterator<? extends Object> iteratorChilds = (Iterator<? extends Object>) oChilds;
					for (; iteratorChilds.hasNext();) {
						Object child = iteratorChilds.next();
						recurseTreeCallMethod(child, mGetChildsMethod, callMethodTarget, mCallMethodAtLevel,
								additionalParametersToTargetMethod);
					}
				} else if (oChilds.getClass().isArray()) {
					Object[] arrayChilds = (Object[]) oChilds;
					for (Object child : arrayChilds) {
						recurseTreeCallMethod(child, mGetChildsMethod, callMethodTarget, mCallMethodAtLevel,
								additionalParametersToTargetMethod);
					}
				} else {
					recurseTreeCallMethod(oChilds, mGetChildsMethod, callMethodTarget, mCallMethodAtLevel,
							additionalParametersToTargetMethod);
				}
			}

			pathRecurseComponents.remove(rootOfTree);
		} catch (IllegalArgumentException e) {
			TreeRecurseException e2 = new TreeRecurseException("illegal argument invoking method", pathRecurseComponents, e);
			pathRecurseComponents.clear();
			throw e2;
		} catch (IllegalAccessException e) {
			TreeRecurseException e2 = new TreeRecurseException("illegal access invoking method", pathRecurseComponents, e);
			pathRecurseComponents.clear();
			throw e2;
		} catch (InvocationTargetException e) {
			TreeRecurseException e2 = new TreeRecurseException("target object does not have method", pathRecurseComponents, e);
			pathRecurseComponents.clear();
			throw e2;
		}

	}

	public void recurseTreeCallMethod(Object rootOfTree, String strGetChildsMethod, Object callMethodTarget,
			String strCallMethodAtLevel, Class[] additionalParametersTypesToTargetMethod,
			Object[] additionalParametersToTargetMethod) throws TreeRecurseException {
		try {
			Method mCallMethodAtLevel = null;
			if (callMethodTarget != null) {
				// There is a callMethodTarget, so the object must
				// allways be the same being called, and we have to pass in the
				// Object at the correct level
				Class[] argTypes =
						new Class[1 + (additionalParametersTypesToTargetMethod == null ? 0 : additionalParametersTypesToTargetMethod.length)];
				argTypes[0] = rootOfTree.getClass();
				if (additionalParametersTypesToTargetMethod != null) {
					System.arraycopy(additionalParametersTypesToTargetMethod, 0, argTypes, 1,
							additionalParametersTypesToTargetMethod.length);
				}

				mCallMethodAtLevel = callMethodTarget.getClass().getMethod(strCallMethodAtLevel, argTypes);
			} else {
				// There is no callMethodTarget, so the method should be called
				// on each
				// object in the tree (so instrospect it from the tree Object -
				// we don't expect to pass in the Object itself, as the method
				// is being
				// called on the correct object allready)
				Class[] argTypes =
						new Class[(additionalParametersTypesToTargetMethod == null ? 0 : additionalParametersTypesToTargetMethod.length)];
				if (additionalParametersTypesToTargetMethod != null) {
					System.arraycopy(additionalParametersTypesToTargetMethod, 0, argTypes, 0,
							additionalParametersTypesToTargetMethod.length);
				}

				mCallMethodAtLevel = rootOfTree.getClass().getMethod(strCallMethodAtLevel, argTypes);
			}

			Method mGetChilds = rootOfTree.getClass().getMethod(strGetChildsMethod);

			recurseTreeCallMethod(rootOfTree, mGetChilds, callMethodTarget, mCallMethodAtLevel,
					additionalParametersToTargetMethod);
		} catch (SecurityException e) {
			TreeRecurseException e2 = new TreeRecurseException("security exception occured", pathRecurseComponents, e);
			pathRecurseComponents.clear();
			throw e2;
		} catch (NoSuchMethodException e) {
			TreeRecurseException e2 = new TreeRecurseException("unable to instrospect method", pathRecurseComponents, e);
			pathRecurseComponents.clear();
			throw e2;
		}
	}

	public void recurseTreeCallMethod(Object rootOfTree, String strGetChildsMethod, Object callMethodTarget,
			String strCallMethodAtLevel) throws TreeRecurseException {
		recurseTreeCallMethod(rootOfTree, strGetChildsMethod, callMethodTarget, strCallMethodAtLevel, null, null);
	}

	public void recurseTreeCallMethod(Object rootOfTree, String strGetChildsMethod, String strCallMethodAtLevel)
			throws TreeRecurseException {
		recurseTreeCallMethod(rootOfTree, strGetChildsMethod, null, strCallMethodAtLevel, null, null);
	}

	public void recurseTreeCallMethod(Object rootOfTree, String strGetChildsMethod, Object callMethodTarget,
			Method mCallMethodAtLevel) throws TreeRecurseException {

		try {
			recurseTreeCallMethod(rootOfTree, rootOfTree.getClass().getMethod(strGetChildsMethod, new Class[0]),
					callMethodTarget, mCallMethodAtLevel, null);
		} catch (SecurityException e) {
			TreeRecurseException e2 = new TreeRecurseException("security exception occured", pathRecurseComponents, e);
			pathRecurseComponents.clear();
			throw e2;
		} catch (NoSuchMethodException e) {
			TreeRecurseException e2 = new TreeRecurseException("unable to instrospect method", pathRecurseComponents, e);
			pathRecurseComponents.clear();
			throw e2;
		}
	}

	public Collection<? extends Object> recurseToRoot(Object child, String strGetParentMethod) throws TreeRecurseException {
		class PathSaver {
			private ArrayList<Object> path = new ArrayList<Object>();

			public Collection<? extends Object> getTotalPath() {
				ArrayList<Object> pathReversed = new ArrayList<Object>();
				pathReversed.addAll(path);
				Collections.reverse(pathReversed);
				return pathReversed;
			}

			public void addPathElement(Object element) {
				path.add(element);
			}
		}

		PathSaver thePathSaver = new PathSaver();

		recurseTreeCallMethod(child, strGetParentMethod, thePathSaver, "addPathElement");

		return thePathSaver.getTotalPath();
	}

	public class TreeRecurseException extends Exception {
		private ArrayList<Object> path;

		public TreeRecurseException(String message, Collection<? extends Object> pathOfError, Throwable rootCause) {
			super(message, rootCause);
			this.path = new ArrayList<Object>();
			this.path.addAll(pathOfError);
		}

		@Override
		public String getMessage() {
			StringBuilder pathBuffer = new StringBuilder();
			for (Object o : path) {
				pathBuffer.append("/");
				pathBuffer.append(o);
			}
			return super.getMessage() + " error occurred at path " + pathBuffer.toString();

		}
	}

}
