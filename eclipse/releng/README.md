# Release Engineering Support

This project includes target platform definition files for Eclipse to support running and debugging
the feature and plugins in Eclipse. These files are used to make sure we maintain backwards compatibility.

| Target File | Our Plugin Version | Notes                                                  |
|-------------|--------------------|--------------------------------------------------------|
| 2018-12     | 1.4.1              | First Eclipse version that supports Java 11 in the AST |
| 2019-12     | 1.4.2?             | Normally we would use this version for 1.4.2, but it won't support M1 if we do |
| 2021-06     | 1.4.2?             | First Eclipse version that supports M1 Mac             |
| 2021-12     | 1.4.2?             | First Eclipse version that supports Java 17 in the AST |

Find p2 Repository sites for the various releases here: https://wiki.eclipse.org/Simultaneous_Release
